import json
import requests
import numpy as np
from discreteMarkovChain import markovChain
from BaseHTTPServer import BaseHTTPRequestHandler,HTTPServer
from urlparse import urlparse, parse_qs
from django.http import HttpResponse
from cssselect import GenericTranslator, SelectorError
from lxml.etree import fromstring
from bs4 import BeautifulSoup
import matplotlib.pyplot as plt
import matplotlib.mlab as mlab
import plotly.plotly as py
import scipy.stats

PORT_NUMBER = 8080


def calcMarkovInv(request):
    print "calculating markov chain ..."
    print(request.GET['abour'])
    startVector = np.array([1./3.,1./3.,1./3.])

    #print startVector

    P = np.array([
            [0, 2./7., 3./7., 2./7.],
            [1./2., 0, 1./2., 0 ],
            [0, 4./5., 0, 1./5.],
            [1./6., 3./6., 2./6., 0]])
    mc = markovChain(P)
    mc.computePi('linear') #We can also use 'power', 'krylov' or 'eigen'
    #print(mc.pi)

    return HttpResponse(json.dumps(mc.pi.tolist()))

def crawlTeams(request):
    teams = [
        'Dortmund' , 'K&ouml;ln',
        'Leverkusen' ,'Ingolstadt',
        'Wolfsburg' ,'Stuttgart',
        'Augsburg' ,'HSV',
        'Bremen' ,'E. Frankfurt',
        'Hoffenheim' ,'Schalke',
        'Mainz' , 'Hertha',
        'Darmstadt', 'Gladbach',
        'Bayern', 'Hannover']

    timeSlot = request.GET['timeSlot']
    print "Predicting the 6th match"
    html = requests.get("http://www.kicker.de/news/fussball/bundesliga/spieltag/1-bundesliga/2015-16/-1/0/spieltag.html")
    #print html.text
    #expression = GenericTranslator().css_to_xpath('div.content')
    soup = BeautifulSoup(html.text, 'html.parser')
    games = soup.find('div', {'id': 'ctl00_PlaceHolderContent_begegnungenCtrl'}).findAll('tr', {'class' : 'fest'})
    #document = fromstring(html.text)
    #print [e.get('id') for e in document.xpath(expression)]
    goalMatrix = np.zeros([len(teams), len(teams)])

    for game in range(len(games))[0:int(timeSlot)*9]:
        data = games[game].findAll('td')
        dateTime = data[1].text
        #print dateTime
        home = data[2].findAll('a')[1].decode_contents(formatter="html")
        away = data[4].findAll('a')[1].decode_contents(formatter="html")
        score = data[5].text
        #print home, away
        #print score.split()[0][0]

        goalMatrix[teams.index(home)][teams.index(away)] = int(score.split()[0][0])
        goalMatrix[teams.index(away)][teams.index(home)] = int(score.split()[0][2])
        #print home, away, score



    relativeGoalMatrix = (goalMatrix.T / goalMatrix.sum(axis=1)).T
    #print relativeGoalMatrix

    mc = markovChain(relativeGoalMatrix)
    mc.computePi('linear') #We can also use 'power', 'krylov' or 'eigen'
    #print(mc.pi)
    teamRank = mc.pi.tolist()

    result = "<h2> Value of Teams </h2> <p> less is better </p>"

    for team in range(len(teams)):
        result += teams[team] + " : " + str(teamRank[team]) + "<br>"

    result += "<br><h2> Home vs Against Goal Value: </h2>"
    for home in range(len(teams)):
        for away in range(len(teams)):
            result += teams[home] + " vs " +  teams[away] + " : " + str(teamRank[home]/teamRank[away]) + "<br>"

    #calculate weighted goals
    goalWeightedMatrix = np.zeros([len(teams), len(teams)])
    for home in range(len(teams)):
        for away in range(len(teams)):
            goalWeightedMatrix[home][away] = goalMatrix[home][away] * teamRank[home]/teamRank[away]

    print goalWeightedMatrix

    #predict games

    result += "<br> <h2> Predictions of game: " + timeSlot + "</h2> <br>"
    durchGoals = goalWeightedMatrix.sum(axis=1)/int(timeSlot)

    print durchGoals
    for game in range(len(games))[int(timeSlot)*9:(int(timeSlot)+1)*9]:
        data = games[game].findAll('td')
        dateTime = data[1].text
        #print dateTime
        home = data[2].findAll('a')[1].decode_contents(formatter="html")
        away = data[4].findAll('a')[1].decode_contents(formatter="html")
        score = data[5].text

        awayIndex = teams.index(away)
        homeIndex = teams.index(home)

        homePrediction = str(teamRank[awayIndex]/teamRank[homeIndex] * durchGoals[homeIndex])
        awayPrediction = str(teamRank[homeIndex]/teamRank[awayIndex] * durchGoals[awayIndex])

        result += teams[homeIndex] + " vs " +  teams[awayIndex] + " : " + str(teamRank[homeIndex]/teamRank[awayIndex]) + " <br> predicted: "+ homePrediction + " : " + awayPrediction + " actual: " + score + "<br>"
        x = np.linspace(-3, 3, 100)

        predictionHome = np.zeros(5)
        predictionAway = np.zeros(5)
        predictionHome[0] =  scipy.stats.norm(float(homePrediction), 1).cdf(1)
        for i in range(5):

            predictionHome[i] =  scipy.stats.norm(float(homePrediction), 1).cdf(i+1) - scipy.stats.norm(float(homePrediction), 1).cdf(i)
            predictionAway[i] =  scipy.stats.norm(float(awayPrediction), 1).cdf(i+1) - scipy.stats.norm(float(awayPrediction), 1).cdf(i)
            #print scipy.stats.norm(float(homePrediction), 1).cdf(3) - scipy.stats.norm(float(homePrediction), 1).cdf(2)

        gameEndLikelihood = np.dot(np.mat(predictionHome).T,(np.mat(predictionAway)))
        likelihoodWin, likelihoodDraw, likelihoodLoss  = calcEnd(gameEndLikelihood)
        result += "win: " +str(likelihoodWin) + "draw: " +str(likelihoodDraw)+ " loss: "+ str(likelihoodLoss) + "<br>"
        plt.plot(x,(mlab.normpdf(x, float(homePrediction), 1)-mlab.normpdf(x, float(awayPrediction), 1)))
        plt.plot(x,mlab.normpdf(x, float(homePrediction), 1))
        plt.plot(x,mlab.normpdf(x, float(awayPrediction), 1))
        plt.show()

    #plotTeams(teamRank)

    return HttpResponse(result)

def calcEnd(gameEndLikelihood):
    draw = 0.
    win = 0.
    loss = 0.

    print gameEndLikelihood
    for i in range(gameEndLikelihood.shape[0]):
        for j in range(gameEndLikelihood.shape[1]):
            if i < j:
                loss += gameEndLikelihood[i,j]
            if j < i:
                win += gameEndLikelihood[i,j]
            if i == j:
                draw += gameEndLikelihood[i,j]

    return win, draw, loss

def plotTeams(teamRank):

    bubbles_mpl = plt.figure()

    # doubling the width of markers
    x = [0,2,4,6,8,10]
    y = [0]*len(x)
    s = [20*4**n for n in range(len(x))]
    plt.scatter(x,y,s=s)

    plot_url = py.plot_mpl(bubbles_mpl, filename='mpl-bubbles')
#This class will handles any incoming request from
#the browser
class myHandler(BaseHTTPRequestHandler):

	#Handler for the GET requests
	def do_GET(self):
		self.send_response(200)
		self.send_header('Content-type','text/html')
		self.end_headers()
        #print parse_qs(urlparse(self.path).query)
		# Send the html message
		self.wfile.write(calcMarkovInv(parse_qs(urlparse(self.path).query)))
		return
"""
try:
	#Create a web server and define the handler to manage the
	#incoming request
	server = HTTPServer(('', PORT_NUMBER), myHandler)
	print 'Started httpserver on port ' , PORT_NUMBER

	#Wait forever for incoming htto requests
	server.serve_forever()

except KeyboardInterrupt:
	print '^C received, shutting down the web server'
	server.socket.close()
"""

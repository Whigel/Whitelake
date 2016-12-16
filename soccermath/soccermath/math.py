import numpy as np
from discreteMarkovChain import markovChain
from BaseHTTPServer import BaseHTTPRequestHandler,HTTPServer
from urlparse import urlparse, parse_qs
from django.http import HttpResponse
import json

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

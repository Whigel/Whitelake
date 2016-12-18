#Whitelake


## Thoughts on how to calculate the score

- Probabilistic

The number of goals shot and received per game are gaussion distributed.
Thats why we  can calculate the final score through:
if x are the number of goals to be Shot by team X and y for Y:

Calculate the variance.

calculate percentage of x = 0 and y = 0, x = 0 y= 1 ... until a threshold is reached

## How to set up django

Currently using python2.7.
What the django server will do for us is crawling the team statistics, dump them into a database and will do the maths.
Later maybe some machine learning stuff.

### Setup Virtual env

```
virtualenv env
. env/bin/activate
```

### Install dependencies
It is pretty simple.

```
sudo apt-get install libxml2-dev libxslt-dev python-dev
```
For better understanding here you will find (explanation)[http://lxml.de/installation.html]
```
pip install -r .dependencies
```

### Run django server

```
python manage.py runserver
```

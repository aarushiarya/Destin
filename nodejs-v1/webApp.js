var port = process.env.PORT || 3000,
    http = require('https'),
    fs = require('fs'),
    html = fs.readFileSync('index.html');

var express = require('express');
var app = express();

var log = function(entry) {
    fs.appendFileSync('/tmp/sample-app.log', new Date().toISOString() + ' - ' + entry + '\n');
};

var server = http.createServer(function (req, res) {
    if (req.method === 'POST') {
        var body = '';

        req.on('data', function(chunk) {
            body += chunk;
        });

        req.on('end', function() {
            if (req.url === '/') {
                log('Received message: ' + body);
            } else if (req.url = '/scheduled') {
                log('Received task ' + req.headers['x-aws-sqsd-taskname'] + ' scheduled at ' + req.headers['x-aws-sqsd-scheduled-at']);
            }

            res.writeHead(200, 'OK', {'Content-Type': 'text/plain'});
            res.end();
        });
    } else {
        res.writeHead(200);
        res.write(html);
        res.end();
    }
});

//nearby search places
app.get('/getPlaces/:keyw/:cat/:dist/:lat/:lon/:apiKey', function (req, result) {
    var data = {
        "detail" : {
            "keyw": req.params.keyw,
            "cat": req.params.cat,
            "dist": req.params.dist,
            "lat": req.params.lat,
            "lon": req.params.lon,
            "apiKey": req.params.apiKey
        }
    };
    console.log(data);
    var url= "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+req.params.lat+","+req.params.lon+"&radius="+req.params.dist+"&type="+req.params.cat+"&keyword="+req.params.keyw+"&key="+req.params.apiKey;
    http.get(url, function(res) {
        console.log('STATUS: ' + res.statusCode);
        console.log('HEADERS: ' + JSON.stringify(res.headers));
        res.setEncoding('utf8');
        var body = '';
        res.on('data', function(chunk) {
            body += chunk;
        });
      
    }).end();
});
// Listen on port 3000, IP defaults to 127.0.0.1
app.listen(port);

// Put a friendly message on the terminal
console.log('Server running at http://127.0.0.1:' + port + '/');
var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');

var __dirname = './demo-shopping-cart-prj/dist/demo-shopping-cart-prj/';

var app = express();
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(__dirname));

app.get('/*', function (req, res) { res.sendFile(path.join(__dirname, 'index.html')) });

// catch 404 and forward to error handler
app.use(function (req, res, next) {
    var err = new Error('Not Found');
    err.status = 404;
    next(err);
});

if (process.env.env == 'development') {
    app.listen(3000, function () {
        console.log('Example listening on port 3000!');
    });
}
else {
    app.listen(4200, function () {
        console.log('Example listening on port 4200!');
    });
}
module.exports = app;
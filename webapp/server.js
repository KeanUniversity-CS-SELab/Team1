const express = require('express');
const app = express();

app.set('view engine', 'html');

app.get('/', (req, res) => {
    res.sendFile(__dirname+'/views/index.html');
});

app.listen(8000, () => {console.log("WebApp listening on port 8000")});
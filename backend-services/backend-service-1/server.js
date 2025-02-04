const express = require('express');
const app = express();

app.get('/api/data', (req, res) => {
    res.json({ service: "Backend Service 1", message: "Hello from Node.js backend!" });
});

app.listen(8000, () => console.log("Backend Service 1 running on port 8000"));

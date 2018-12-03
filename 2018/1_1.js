const readline = require('readline');

const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});
count = 0;
rl.on('line', input => {
    count += +input;
})
rl.on('SIGINT', () => {
    console.log("Result:" + count);
    rl.close();
});

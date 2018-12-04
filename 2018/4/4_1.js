// order input
// find guard that falls most asleep

const fs = require('fs');
let guardsAsleepTime = {};
let guardsSleepingMinutes = {};
let maxSleepTime = 0;
let maxSleepGuard;

fs.readFile('input','utf8', (err,data) => {
    let lines = data.split('\r\n');
    lines.sort();
    let guardId, startSleep;
    
    lines.forEach(line => {
        let time = getTime(line);
        let matchResult = line.match(/#(\d+)/);
        if (matchResult){
            guardId = +matchResult[1];
        } else {
            if (line.indexOf('wakes') > 0){
                // wakes up
                if (guardsAsleepTime[guardId]){
                    guardsAsleepTime[guardId]+= time.minute - startSleep;
                } else {
                    guardsAsleepTime[guardId] = time.minute - startSleep;
                }
                if (guardsAsleepTime[guardId] > maxSleepTime){
                    maxSleepTime = guardsAsleepTime[guardId];
                    maxSleepGuard = guardId;
                }
                fillGuardSleepingMinutes(guardId, startSleep, time.minute);
            } else{
                // fall asleep
                startSleep = time.minute;
            }
        }
    });
    console.log(`Max Sleep guard: ${maxSleepGuard} with ${maxSleepTime} minutes sleeping`);
    let maxMinuteOverlapCount = Math.max(...guardsSleepingMinutes[maxSleepGuard]);
    let maxMinuteOverlap = guardsSleepingMinutes[maxSleepGuard].indexOf(maxMinuteOverlapCount);
    console.log('Max sleeping overlap minute: ', maxMinuteOverlap);
    console.log('Max overlap sleeping count: ', maxMinuteOverlapCount);
    console.log('Awnser: ', maxSleepGuard * maxMinuteOverlap);
});

function fillGuardSleepingMinutes(guard, startMinute, wakeMinute){
    if (guardsSleepingMinutes[guard] == null){
        guardsSleepingMinutes[guard]= new Array(60);
        for (let i = 0; i < 60; i++){
            guardsSleepingMinutes[guard][i] = 0;
        }
    }
    let i = startMinute;
    while(i < wakeMinute){
        guardsSleepingMinutes[guard][i]++;
        i++;
    }
}

function getTime(line){
    let time = line.match(/\[\d{4}-\d{2}-\d{2}\s(\d{2}):(\d{2})\]/);
    return {
        hour: +time[1],
        minute: +time[2]
    }
}
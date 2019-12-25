import React from "react";
import {Button} from "react-bootstrap";

const DeviceIndicator = ({device, restartPiTemp, restartDHT}) => {

    const isPiTempRunning = () => {
       if(device.piTempStart !== ''){
           return 'running';
       }
       return 'not running';
    }

    const isDHTRunning = () => {
        if(device.dhtStart !== ''){
            return 'running';
        }
        return 'not running';
    }

    return(
        <div>
      <div className={"deviceIndicator"}>
          <p><b>Device Status:</b> {device.running} <b>|</b> <b>Pitemp Status:</b> {` ${isPiTempRunning()}`} <b>|</b> <b>DHT Status:</b>{` ${isDHTRunning()}`}
          </p>

      </div>
        <div className={"deviceIndicator"}>
        <p>
        <Button variant={"primary"} onClick={restartPiTemp}>Restart PiTemp</Button>
    <Button variant={"primary"} onClick={restartDHT}>Restart DHT</Button>
    </p>
        </div>
        </div>
    );

}
export default DeviceIndicator
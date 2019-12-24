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
      <div className={"deviceIndicator"}>
          <p>'Device: {device.name}'</p>
          <p>Pitemp Status: {isPiTempRunning}</p>
          <Button variant={"primary"} onClick={restartPiTemp}>Restart PiTemp</Button>
          <p>DHT Status: {isDHTRunning}</p>
          <Button variant={"primary"} onClick={restartDHT}>Restart DHT</Button>
      </div>
    );

}
export default DeviceIndicator
import React from "react";
import {Button} from "react-bootstrap";

import Loader from 'react-loader-spinner';

const DeviceIndicator = ({user, device, restartPiTemp, restartDHT, restartPi, deviceLoading}) => {

    const isPiTempRunning = () => {
        if (device.piTempStart !== '') {
            return 'running';
        }
        return 'not running';
    }

    const isDHTRunning = () => {
        if (device.dhtStart !== '') {
            return 'running';
        }
        return 'not running';
    }

    return (
        <div>
            <div className={"deviceIndicator"}>
                <p><b>Device Status:</b> {device.running} <b>|</b> <b>Pitemp Status:</b> {` ${isPiTempRunning()}`}
                    <b>|</b> <b>DHT Status:</b>{` ${isDHTRunning()}`}
                </p>

            </div>
            {deviceLoading && (
                <div className={"deviceIndicator"}>
                    <Loader type={"Rings"} color={"#1976D2"} height={80} width={80} />
                </div>
            )}
            {!deviceLoading && (
                <div className={"deviceIndicator"}>
                    <p>
                        <Button variant={"primary"} onClick={() => restartPi(user, device.name)}>Restart Device</Button>
                        <Button variant={"primary"} onClick={() => restartPiTemp(user, device.name)}>Restart PiTemp</Button>
                        <Button variant={"primary"} onClick={() => restartDHT(user, device.name)}>Restart DHT</Button>
                    </p>
                </div>
            )}
        </div>
    );

}
export default DeviceIndicator
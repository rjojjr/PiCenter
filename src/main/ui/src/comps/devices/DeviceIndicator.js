import React from "react";
import {Button} from "react-bootstrap";

import Loader from 'react-loader-spinner';
import {isDeviceInLoading} from "../../services/device-service";

const DeviceIndicator = ({user, device, restartPiTemp, restartDHT, restartPi, deviceLoading, devices}) => {

    const isPiTempRunning = () => {
        if (device.piTempStart !== '' && device.piTempStart !== 'r') {
            return 'running';
        }else if (device.piTempStart === 'r') {
            return 'restarting';
        }
        return 'not running';
    }

    const isDHTRunning = () => {
        if (device.dhtStart !== '' && device.dhtStart !== 'r') {
            return 'running';
        }else if (device.dhtStart === 'r') {
            return 'restarting';
        }
        return 'not running';
    }

    const deviceIsLoading = () => {
        return isDeviceInLoading(deviceLoading, device.name);
    }

    return (
        <div>
            <div className={"deviceIndicator"}>
                <p><b>Device Status:</b> {device.running} <b>|</b> <b>Pitemp Status:</b> {` ${isPiTempRunning()}`}
                    <b>|</b> <b>DHT Status:</b>{` ${isDHTRunning()}`}
                </p>

            </div>
            {deviceIsLoading() && (
                <div className={"deviceIndicator"}>
                    <Loader type={"ThreeDots"} color={"#1976D2"} height={80} width={80} />
                </div>
            )}
            {!deviceIsLoading() && (
                <div className={"deviceIndicator"}>
                    <p>
                        <Button variant={"primary"} onClick={() => restartPi(user, device.name)}>Restart Device</Button>
                        <Button variant={"primary"} onClick={() => restartPiTemp(user, device.name, device, devices)}>Restart PiTemp</Button>
                        <Button variant={"primary"} onClick={() => restartDHT(user, device.name, device, devices)}>Restart DHT</Button>
                    </p>
                </div>
            )}
        </div>
    );

}
export default DeviceIndicator
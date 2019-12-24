import React from "react";
import DeviceIndicator from "./DeviceIndicator";
import {Button} from "react-bootstrap";

const DeviceStatusPage = ({getStatuses, restartPiTemp, restartDHT, deviceData}) => {

    return (
        <div className={"page deviceStatusPage"}>
            <div className={"deviceIndicatorContainer"}>
                {deviceData.map(device => {
                    return (
                        <DeviceIndicator device={device}
                                         restartPiTemp={restartPiTemp}
                                         restartDHT={restartDHT}/>
                    );
                })}
            </div>
            <Button variant={"primary"} onClick={getStatuses}>Refresh</Button>
        </div>
    );

}
export default DeviceStatusPage
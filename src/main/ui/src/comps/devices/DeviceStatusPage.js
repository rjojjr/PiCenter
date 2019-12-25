import React from "react";
import DeviceIndicator from "./DeviceIndicator";
import {Button, Card} from "react-bootstrap";

const DeviceStatusPage = ({user, getStatuses, restartPiTemp, restartDHT, deviceData}) => {

    const cardColor  = (device) => {
        if(device.running === "running" && device.piTempStart !== '' && device.dhtStart !== ''){
            return "light";
        } else {
            return "warning";
        }
    }

    return (
        <div className={"page deviceStatusPage"}>
            <div className={"deviceIndicatorContainer"}>
                {deviceData.map(device => {
                    return (
                                <Card bg={cardColor(device)} style={{ width: '50rem' }}>
                                    <Card.Title><b>Device:</b> {device.name}</Card.Title>
                                    <Card.Body>
                                        <DeviceIndicator device={device}
                                                         restartPiTemp={restartPiTemp}
                                                         restartDHT={restartDHT}
                                                        user={user}/>
                                    </Card.Body>
                                </Card>

                    );
                })}
            </div>
            <Button variant={"primary"} onClick={() => getStatuses(user)}>Refresh</Button>
        </div>
    );

}
export default DeviceStatusPage
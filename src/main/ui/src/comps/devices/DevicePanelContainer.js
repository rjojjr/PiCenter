import React, {useEffect} from "react";

import {changePage, logOff} from "../../actions/universal-actions";
import {connect} from "react-redux";
import * as pageConstants from "../../constants/page-constants";
import {
    getStatusesThunk,
    isDeviceError,
    isDevicesLoading,
    restartDHTThunk,
    restartPiTempThunk, restartPiThunk
} from "../../actions/device-actions";
import DeviceStatusContainer from "./DeviceStatusContainer";

const DevicePanelContainer = ({user, changePage, isLoading, isError, errorMsg, isDeviceLoading, isDeviceError, getStatuses, restartPiTemp, restartDHT, logOff, deviceData, restartPi, devicesLoading}) => {

    useEffect(() => {
        getStatuses(user);
    }, [getStatuses]);

    return (
        <div className="pageContainer devicePanelContainer">
            {user.page === pageConstants.PI_STATUSES && (
                <DeviceStatusContainer user={user}
                                       changePage={changePage}
                                       isLoading={isLoading}
                                       isError={isError}
                                       errorMsg={errorMsg}
                                       isDeviceLoading={isDeviceLoading}
                                       isDeviceError={isDeviceError}
                                       getStatuses={getStatuses}
                                       restartPiTemp={restartPiTemp}
                                       restartDHT={restartDHT}
                                       logOff={logOff}
                                       deviceData={deviceData}
                                       restartPi={restartPi}
                                       devicesLoading={devicesLoading}/>
            )}
        </div>
    )

};

const mapStateToProps = state => ({
    user: state.user,
    isLoading: state.isDeviceLoading,
    isError: state.isDeviceError,
    errorMsg: state.deviceMsg,
    deviceData: state.deviceData,
    devicesLoading: state.devicesLoading
});

const mapDispatchToProps = {
    logOff,
    changePage,
    isDeviceLoading: isDevicesLoading,
    isDeviceError,
    getStatuses: getStatusesThunk,
    restartPiTemp: restartPiTempThunk,
    restartDHT: restartDHTThunk,
    restartPi: restartPiThunk
};

export default connect(mapStateToProps, mapDispatchToProps)(DevicePanelContainer);
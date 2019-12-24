import React, {useEffect} from "react";

import {changePage, logOff} from "../../actions/universal-actions";
import {connect} from "react-redux";
import * as pageConstants from "../../constants/page-constants";
import {
    getStatusesThunk,
    isDeviceError,
    isDeviceLoading,
    restartDHTThunk,
    restartPiTempThunk
} from "../../actions/device-actions";
import DeviceStatusContainer from "./DeviceStatusContainer";

const DevicePanelContainer = ({user, changePage, isLoading, isError, errorMsg, isDeviceLoading, isDeviceError, getStatuses, restartPiTemp, restartDHT, logOff, deviceData}) => {

    useEffect(() => {
        getStatuses();
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
                                       deviceData={deviceData}/>
            )}
        </div>
    )

};

const mapStateToProps = state => ({
    user: state.user,
    isLoading: state.isDeviceLoading,
    isError: state.isDeviceError,
    errorMsg: state.deviceMsg,
    deviceData: state.deviceData
});

const mapDispatchToProps = {
    logOff,
    changePage,
    isDeviceLoading,
    isDeviceError,
    getStatuses: getStatusesThunk,
    restartPiTemp: restartPiTempThunk,
    restartDHT: restartDHTThunk
};

export default connect(mapStateToProps, mapDispatchToProps)(DevicePanelContainer);
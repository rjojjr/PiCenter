import React from "react";


import {updateSession} from "../../services/axios-service";

import {changePage, logOff} from "../../actions/universal-actions";
import {connect} from "react-redux";
import * as pageConstants from "../../constants/page-constants";
import CSVPageContainer from "./CSVPage/CSVPageContainer";
import {
    getStatusesThunk,
    isDeviceError,
    isDeviceLoading,
    restartDHTThunk,
    restartPiTempThunk
} from "../../actions/device-actions";

const DevicePanelContainer = ({user, changePage, isLoading, isError, errorMsg, isDeviceLoading, isDeviceError, getStatuses, restartPiTemp, restartDHT, logOff}) => {

    //updateSession(user.page, user);

    return (
        <div className="pageContainer dataPageContainer">
            {user.page === pageConstants.PI_STATUSES && (
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
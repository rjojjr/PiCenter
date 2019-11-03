import React, {useState} from 'react';
import {connect} from 'react-redux';

import {isLoggingOn, logOnThunk} from "../../actions/logon-actions";
import {resetIsShowMsg} from "../../actions/universal-actions";
import PageSelector from "../PageSelector";
import LogOnPage from "./LogOnPage";

const LogOnPageContainer = ({logOn, message, user, isShowMsg, resetIsShowMsg, isLoggingOn}) => {

    return (
        //Put header here
        <div className={"pageContainer logOnForm"}>
            <LogOnPage user={user} logOn={logOn} resetIsShowMsg={resetIsShowMsg} isShowMsg={isShowMsg} message={message} isLoggingOn={isLoggingOn}/>
        </div>
        //Put footer here
    )
};

const mapStateToProps = state => ({
    message: state.message,
    user: state.user,
    isShowMsg: state.isShowMsg,
    isLoggingOn: state.isLoggingOn
});

const mapDispatchToProps = {
    logOn: logOnThunk,
    resetIsShowMsg: resetIsShowMsg
};

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(LogOnPage);
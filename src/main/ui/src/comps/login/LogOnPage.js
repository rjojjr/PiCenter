import React, {useState} from 'react';
import {connect} from 'react-redux';

import {logOnThunk} from "../../actions/logon-actions";
import {logOn} from "../../services/axios-service";

const LogonPage = ({logOn, message, user, isHowMsg}) => {

    const[username, setUsername] = useState('');

    const handleUsernameOnChange = (e) => {
        setUsername(e.target.value);
    }

};

const mapStateToProps = state => ({
    message: state.message,
    user: state.user,
    isShowMsg: state.isShowMsg
});

const mapDispatchToProps = {
    logOn: logOnThunk
};

export default connect(
    mapDispatchToProps,
    mapStateToProps
)(LogonPage);
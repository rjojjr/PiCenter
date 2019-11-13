import React from 'react';
import {connect} from 'react-redux';
import PageSelector from "./PageSelector";
import * as constants from '../constants/page-constants';
import{ logOff} from '../actions/universal-actions';

const AppContainer = ({user}) => {

    return (
        //Put header here
        <div className={"appContainer"}>
            {user !== {} && user.userName !== 'null'  && user.userName !== undefined && <PageSelector user={user}/>}
            {user === {} || user.userName === 'null'  && <PageSelector user={{page: constants.LOGIN_PAGE}}/>}
        </div>
        //Put footer here
    )
};

const mapStateToProps = state => ({
    user: state.user
});

const mapDispatchToProps = {
    logOff 
};

export default connect(
    mapStateToProps,
    undefined
)(AppContainer);
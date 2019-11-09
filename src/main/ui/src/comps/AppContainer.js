import React from 'react';
import {connect} from 'react-redux';
import PageSelector from "./PageSelector";
import * as constants from '../constants/page-constants';

const AppContainer = ({user}) => {

    return (
        //Put header here
        <div className={"appContainer"}>
            {user !== null && user.userName !== 'null'  && user.userName !== undefined && <PageSelector user={user}/>}
            {user === null || user.userName === 'null'  && <PageSelector user={{page: constants.LOGIN_PAGE}}/>}
        </div>
        //Put footer here
    )
};

const mapStateToProps = state => ({
    user: state.user
});

const mapDispatchToProps = {

};

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(AppContainer);
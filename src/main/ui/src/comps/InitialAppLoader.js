import React, {useEffect} from 'react';
import {connect} from 'react-redux';

import {loadAppThunk} from '../actions/loader-actions';

import PageSelector from './PageSelector';

const InitialAppLoader = ({
                              isError,
                              isShowMsg,
                              loadApp,
                              errorMsg,
                              message,
                              isLoading,
                             user
                          }) => {
    useEffect(() => {
        loadApp();
    }, [loadApp]);

    return (
        <div>
            {isLoading && <p className="message">One moment...</p>}
            {isError &&(
                <p className="message">{errorMsg}</p>
            )}
            {isShowMsg &&(
                <p className="message">{message}</p>
            )}
            {user !== null && <PageSelector user={user}/>}
            {user === null && <PageSelector user={{page: '/logon'} }/>}
        </div>
    );
};

const mapStateToProps = state => ({
    isShowMsg: state.isShowMsg,
    isError: state.isError,
    message: state.message,
    errorMsg: state.errorMsg,
    isLoading: state.isLoading,
    user: state.user
});

const mapDispatchToProps = {
    loadApp: loadAppThunk
};
export default connect(
    mapStateToProps,
    mapDispatchToProps
)(InitialAppLoader);

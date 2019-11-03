import React, {useEffect} from 'react';
import {connect} from 'react-redux';

import {loadAppThunk} from '../actions/loader-actions';

import AppContainer from "./AppContainer";

const InitialAppLoader = ({
                              isError,
                              loadApp,
                              errorMsg,
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
            {/*{isShowMsg &&(
                <p className="message">{message}</p>
            )}*/}
            {!isError && !isLoading && <AppContainer />}
        </div>
    );
};

const mapStateToProps = state => ({
    isError: state.isError,
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

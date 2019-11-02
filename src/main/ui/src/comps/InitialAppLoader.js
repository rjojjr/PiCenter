import React, {useEffect} from 'react';
import {connect} from 'react-redux';

import {loadAppThunk} from './actions/loader-actions';

const InitialAppLoader = ({
    isError,
    isShowMsg,
    loadApp,
    errorMsg,
    message,
    isLoading
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

            {isError &&(
                <p className="message">{errorMsg}</p>
            )}
            {areQuestionsLoaded && <SurveyAppContainer/>}
        </div>
    );
};

const mapStateToProps = state => ({
    isShowMsg: state.isShowMsg,
    isError: state.isError,
    message: state.message,
    errorMsg: state.errorMsg,
    isLoading: state.isLoading
});

const mapDispatchToProps = {
    loadApp: loadAppThunk
};
export default connect(
    mapStateToProps,
    mapDispatchToProps
)(InitialAppLoader);

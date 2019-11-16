import React, {useEffect} from "react";
import {connect} from "react-redux";
import {logOff} from '../../actions/universal-actions';

const UserPageContainer = ({
                               page
                           }) => {

    return (
        <div>
        </div>
    );
};

const mapStateToProps = state => ({
    user: state.user,
    isLoading: state.isSummaryLoading,
    isError: state.isSummaryError,
    errorMsg: state.message
});

const mapDispatchToProps = {
    logOff
};

export default connect(mapStateToProps, mapDispatchToProps)(UserPageContainer);

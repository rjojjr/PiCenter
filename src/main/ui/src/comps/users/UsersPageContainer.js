import React, {useEffect} from "react";
import {connect} from "react-redux";
import {logOff} from '../../actions/universal-actions';
import {usersIsLoading, usersLoadingError} from "../../actions/user-actions";

const UserPageContainer = ({
                               user,
                               isLoading, isError,
                               errorMsg, logOff, usersIsLoading, usersLoadingError
                           }) => {

    return (
        <div>
        </div>
    );
};

const mapStateToProps = state => ({
    user: state.user,
    isLoading: state.isUserLoading,
    isError: state.isUserError,
    errorMsg: state.message
});

const mapDispatchToProps = {
    logOff,
    usersIsLoading,
    usersLoadingError
};

export default connect(mapStateToProps, mapDispatchToProps)(UserPageContainer);


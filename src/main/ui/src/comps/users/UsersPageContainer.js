import React, {useEffect} from "react";
import {connect} from "react-redux";

import {logOff, changePage} from '../../actions/universal-actions';

import {usersIsLoading, usersLoadingError} from "../../actions/user-actions";
import CreateUserPage from "./create-user/CreateUserPage";

import {updateSession} from "../../services/axios-service";

import * as pageConstants from "../../constants/page-constants";

const UserPageContainer = ({
                               user,
                               isLoading, isError,
                               errorMsg, logOff, usersIsLoading, usersLoadingError
                           }) => {

    const tabClickHandler = (tabIndex) => {
        switch (tabIndex) {
            case 0: {
                changePage(pageConstants.CREATE_USER);
            };
            default: {
                changePage(pageConstants.USERS);
            }
        }
    }

    return (
        <div>
            <CreateUserPage changePage={changePage} logOff={logOff} user={user} onClickHandler={tabClickHandler}/>
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
    usersLoadingError,
    changePage
};

export default connect(mapStateToProps, mapDispatchToProps)(UserPageContainer);


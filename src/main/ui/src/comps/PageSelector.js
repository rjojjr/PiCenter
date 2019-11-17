import React from "react";

import * as constants from "../constants/page-constants";

import LoginPageContainer from './login/LogOnPageContainer';
import SummaryPageLoader from "./summary/SummaryPageLoader";

import {updateSession} from "../services/axios-service";
import UserPageContainer from "./users/UsersPageContainer";

const PageSelector = ({user}) =>{
    const page = user.page;

    updateSession(page, user);

    return(
        <div className={"pageSelector"}>
            {page === constants.LOGIN_PAGE && <LoginPageContainer />}
            {page === constants.SUMMARY_PAGE && <SummaryPageLoader />}
            {page.includes(constants.USERS) && <UserPageContainer />}
        </div>
    )
}
export default PageSelector
import React from "react";

import * as constants from "../constants/page-constants";

import LoginPageContainer from './login/LogOnPageContainer';
import SummaryPageLoader from "./summary/SummaryPageLoader";

import {updateSession} from "../services/axios-service";
import UserPageContainer from "./users/UsersPageContainer";
import DataPageContainer from "./data/DataPageContainer";
import DevicePanelContainer from "./devices/DevicePanelContainer";

const PageSelector = ({user}) =>{

    const page = user.page;

    const checkPage = () => {
        if(user !== {} && user.userName !== 'null'  && user.userName !== undefined && user.page === "null"){
            updateSession(constants.SUMMARY_PAGE, user);
        }else {
            updateSession(page, user);
        }
    }

    //checkPage();

    return(
        <div className={"pageSelector"}>
            {page === constants.LOGIN_PAGE && <LoginPageContainer />}
            {page === constants.SUMMARY_PAGE && <SummaryPageLoader />}
            {page.includes(constants.USERS) && <UserPageContainer />}
            {page.includes(constants.DATA) && <DataPageContainer />}
            {page === constants.PI_STATUSES && <DevicePanelContainer />}
        </div>
    )
}
export default PageSelector
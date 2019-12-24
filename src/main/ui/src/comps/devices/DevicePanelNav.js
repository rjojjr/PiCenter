import React from "react";
import * as pageConstants from "../../../constants/page-constants";

const DevicePanelNav = ({changePage}) => {
    return(
        <div className={"nav devicePanelNav"}>
            <ul>
                <li>
                    <ul>
                        <li><b>Device Panel</b> </li>
                    </ul>
                    <ul>
                        <li><a className={'link summaryLink lightText'} onClick={() => changePage(pageConstants.SUMMARY_PAGE)}>Summary</a></li>
                        <li><a className={'link usersLink lightText'} onClick={() => changePage(pageConstants.USERS)}>Users</a></li>
                        <li><a className={'link dataLink lightText'} onClick={() => changePage(pageConstants.DATA)}>Data</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    );
}
export default DevicePanelNav
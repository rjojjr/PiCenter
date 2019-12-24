import React from "react";
import * as pageConstants from "../../../constants/page-constants";

const CSVPageNav = ({changePage}) => {
    return(
        <div className={"nav csvPageNav"}>
            <ul>
                <li>
                    <ul>
                        <li><b>Data</b>
                        <ul>
                            <li>
                                <b>CSV Export</b>
                                <li><a className={'link summaryLink lightText'} onClick={() => changePage(pageConstants.DATA_VISUAL)}>Visual</a></li>
                            </li>
                        </ul>
                        </li>
                        
                    </ul>
                    <ul>
                        <li><a className={'link summaryLink lightText'} onClick={() => changePage(pageConstants.SUMMARY_PAGE)}>Summary</a></li>
                        <li><a className={'link usersLink lightText'} onClick={() => changePage(pageConstants.USERS)}>Users</a></li>
                        <li><a className={'link dataLink lightText'} onClick={() => changePage(pageConstants.PI_STATUSES)}>Device Panel</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    );
}
export default CSVPageNav
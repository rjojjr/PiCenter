import React from "react";
import * as pageConstants from "../../../constants/page-constants";

const VisualPageNav = ({changePage}) => {
    return(
        <div className={"nav visualPageNav"}>
            <ul>
                <li>
                    <ul>
                        <li><b>Data</b>
                            <ul>
                                <li>
                                    <b>Visual</b>
                                    <li><a className={'link summaryLink lightText'} onClick={() => changePage(pageConstants.CSV_EXPORT)}>CSV Export</a></li>
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
export default VisualPageNav
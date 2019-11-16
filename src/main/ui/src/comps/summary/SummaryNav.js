import React from 'react';

import * as pageConstants from "../../constants/page-constants";

const SummaryNav = (changePage) => {
    return (
        <div className={"nav summaryNav"}>
            <ul>
                <li>
                    <ul>
                        <li>
                            <b>Summary</b>
                        </li>
                    </ul>
                    <ul>
                        <li><a onClick={() => changePage(pageConstants.USERS)}>Users</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    );
}

export default SummaryNav;
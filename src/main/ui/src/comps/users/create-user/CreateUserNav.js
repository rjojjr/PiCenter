import React from 'react';

import * as pageConstants from "../../../constants/page-constants";

const CreateUserNav = ({changePage}) => {
    return (
        <div className={"nav createUserNav"}>
            <ul>
                <li>
                    <ul>
                        <li><b>Users</b>
                            <ul>
                                <li>
                                    <b>Create User</b>
                                </li>
                            </ul>
                        </li>

                    </ul>

                    <ul>
                        <li><a className={'link summaryLink lightText'} onClick={() => changePage(pageConstants.SUMMARY_PAGE)}>Summary</a></li>
                        <li><a className={'link dataLink lightText'} onClick={() => changePage(pageConstants.DATA)}>Data</a></li>
                        <li><a className={'link dataLink lightText'} onClick={() => changePage(pageConstants.PI_STATUSES)}>Device Panel</a></li>
                    </ul>
                </li>

            </ul>
        </div>
    );
}

export default CreateUserNav;
import React, {useState} from "react";
import SummaryPage from "./SummaryPage";
import SummaryPageHeader from "./SummaryPageHeader";
import LoadingView from "../global/LoadingView";

import * as pageConstants from '../../constants/page-constants';

import {changePage} from "../../actions/universal-actions";

const SummaryPageContainer = ({
                                  summary,
                                  user,
                                  isError,
                                  errorMsg,
                                  isLoading,
                                  canLoad,
                                  logOff
                              }) => {
    const [sensorIndex, setSensorIndex] = useState(0);

    const selectSensor = index => {
        setSensorIndex(index);
    };

    const showSummary = () => {
        if (summary.length === 0) {
            return "";
        } else {
            return summary[sensorIndex];
        }
    };

    return (
        <div className={"container summaryPageContainer"}>
            <LoadingView isLoading={isLoading} message={"Loading.."}/>
            {canLoad && !isLoading && (
                <div>
                    <header>
                        <h2>PiCenter Sensor Summary</h2>

                    </header>
                    <div id="main">
                        <section>
                            <header>
                                <SummaryPageHeader
                                    isLoading={isLoading}
                                    summary={summary}
                                    selectSensor={selectSensor}
                                />

                            </header>
                            <SummaryPage
                                canRender={canLoad}
                                isLoading={isLoading}
                                summary={showSummary()}
                            />
                        </section>
                        <nav>
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
                        </nav>
                        <aside>
                            <h4>Logged on as: {user.userName}</h4>
                            <button type={"button"} onClick={logOff}>
                                Logout
                            </button>
                        </aside>
                    </div>
                    <footer>
                        <a href={"github.com/rjojjr"}>Visit me on github</a>
                    </footer>
                </div>
            )}
        </div>
    );
};

export default SummaryPageContainer;

import React, {useState} from "react";
import SummaryPage from "./SummaryPage";
import SummaryPageHeader from "./SummaryPageHeader";
import LoadingView from "../global/LoadingView";
import Button from 'react-bootstrap/Button';


import * as pageConstants from '../../constants/page-constants';
import SummaryNav from "./SummaryNav";

const SummaryPageContainer = ({
                                  summary,
                                  user,
                                  isError,
                                  errorMsg,
                                  isLoading,
                                  canLoad,
                                  logOff,
                                  changePage
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
        <div className={"pageContainer summaryPageContainer"}>
            <LoadingView isLoading={isLoading} message={"Loading.."}/>
            {canLoad && !isLoading && (
                <div className={"pageContainer"}>
                    <div>
                        <header>
                            <h2>PiCenter Sensor Summary</h2>

                        </header>
                    </div>
                    <div id="main">
                        <div className={"scrollPage"}>
                            <section className={"summaryPage"}>
                                <header className={"summaryPage"}>
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
                        </div>
                        <nav className={"summaryPage"}>
                            <SummaryNav changePage={changePage}/>
                        </nav>
                        <aside className={"summaryPage"}>
                            <h4>Logged on as: {user.userName}</h4>
                            <Button variant={"primary"} type={"button"} onClick={logOff}>
                                Logout
                            </Button>
                        </aside>
                    </div>
                    <div>
                        <footer>
                            <a className={"lightText"} href={"http://github.com/rjojjr"}>Visit me on github</a>
                        </footer>
                    </div>
                </div>
            )}
        </div>
    );
};

export default SummaryPageContainer;

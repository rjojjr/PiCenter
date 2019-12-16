import LoadingView from "../../global/LoadingView";
import GenericPageHeader from "../../global/GenericPageHeader";
import VisualPageNav from "./VisualPageNav";
import * as pageConstants from "../../../constants/page-constants";
import React from "react";
import Button from 'react-bootstrap/Button';
import VisualPage from "./VisualPage";
import {dateStringFormat} from "../../../services/helper-service";

const VisualPageContainer = ({user, changePage, updateSession, isLoading, isError, errorMsg, isDataError, logOff, onClickHandler, tempChartStart, tempChartEnd, visualFromDate, visualToDate, chartData, getChart, setChartType, chartType, setChartFlavor, chartFlavor}) => {

    updateSession(pageConstants.DATA_VISUAL, user);

    const getChartHandler = (type, flavor) => {
        getChart(user, dateStringFormat(tempChartStart), dateStringFormat(tempChartEnd), chartType, chartFlavor);
    }

    const convertType = () => {
        if (chartType === 'temp' && chartFlavor === 'avg'){
            return 1;
        }else if (chartType === 'hum' && chartFlavor === 'avg'){
            return 2;
        }else if (chartType === 'temp' && chartFlavor === 'hl'){
            return 3;
        }else{
            return 4;
        }
    }

    return (
        <div className={"pageContainer visualPageContainer"}>
            <LoadingView isLoading={isLoading}/>
            {!isLoading && (
                <div className={"pageContainer"}>
                    <header>
                        <h2>PiCenter Data Page</h2>

                    </header>
                    <div id="main">
                        <section className={"visualPage"}>
                            <header className={"visualPage"}>
                                <GenericPageHeader isLoading={isLoading} currentTabIndex={1} onClickHandler={onClickHandler} tabs={pageConstants.DATA_TABS}/>
                            </header>
                            <p>{errorMsg}</p>
                            <VisualPage user={user} isDataError={isDataError} visualFromDate={visualFromDate} visualToDate={visualToDate} tempChartStart={tempChartStart} tempChartEnd={tempChartEnd} chartData={chartData} getChart={getChartHandler} setChartType={setChartType} chartType={chartType} setChartFlavor={setChartFlavor} chartFlavor={chartFlavor} intSelected={() => convertType()}/>
                        </section>
                        <nav className={"visualPage"}>
                            <VisualPageNav changePage={changePage} />
                        </nav>
                        <aside className={"visualPage"}>
                            <h4>Logged on as: {user.userName}</h4>
                            <Button variant={"primary"} type={"button"} onClick={logOff}>
                                Logout
                            </Button>
                        </aside>
                    </div>
                    <footer>
                        <a className={"lightText"} href={"http://github.com/rjojjr"}>Visit me on github</a>
                    </footer>
                </div>
            )}
        </div>
    )

}
export default VisualPageContainer
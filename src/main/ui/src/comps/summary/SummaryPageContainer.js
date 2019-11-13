import React, { useState } from "react";
import SummaryPage from "./SummaryPage";
import SummaryPageHeader from "./SummaryPageHeader";
import LoadingView from "../global/LoadingView";
import LogOnPage from "../login/LogOnPage";

const SummaryPageContainer = ({
  summary,
  user,
  isError,
  errorMsg,
  isLoading,
  canLoad
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
      <LoadingView isLoading={isLoading} message={"Loading.."} />
      {canLoad &&
        !isLoading && (
          <div>
            <header>
              <h2>PiCenter Sensor Summary</h2>
              <SummaryPageHeader
                  isLoading={isLoading}
                  summary={summary}
                  selectSensor={selectSensor}
              />
            </header>
            <div id="main">
              <section>
                <SummaryPage canRender={canLoad} isLoading={isLoading} summary={showSummary()} />
              </section>
              <nav><b>Summary</b></nav>
              <aside>
                <h4>Logged on as: {user.userName}</h4>
                <button
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

import React, { Fragment } from "react";
import SummaryTableRows from "./SummaryTableRows";
import { isLoading } from "../../actions/loader-actions";

const SummaryPage = ({ summary, user, isLoading, canRender }) => {
  const intervals = [
    "Right Now",
    "1 Hour",
    "2 Hours",
    "3 Hours",
    "6 Hours",
    "12 Hours",
    "24 Hours"
  ];

  return (
    <div className={"page summaryPage"}>
      {!isLoading && (
        <div>
          <h2>Sensor {summary.roomName} Summary</h2>
          <table className={"summaryTable"}>
            <tbody>
              <tr>
                <th>Interval</th>
                <th>Temperature Average</th>
                <th>Humidity Average</th>
                <th>Sample Standard Deviation</th>
              </tr>
              <tr>
                <td></td>
                <td></td>
                <td></td>
                <td><b>Temp</b></td>
                <td><b>Humidity</b></td>

                              </tr>
                {intervals.map((interval, index) => {
                  return (
                      <SummaryTableRows
                        interval={interval}
                        index={index}
                        summary={summary}
                      />
                  );
                })}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

export default SummaryPage;

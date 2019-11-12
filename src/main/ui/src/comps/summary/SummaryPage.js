import React, { Fragment } from "react";
import SummaryTableRows from "./SummaryTableRows";

const SummaryPage = ({ summary, user }) => {
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
      <h2>`Sensor ${summary.roomName} Summary`</h2>
      <table>
        <tbody>
          <tr>
            <th>Time</th>
            <th>Temperature Average</th>
            <th>Humidity Average</th>
            <th>Temp & Humidity Sample Standard Deviation</th>
          </tr>
          <tr>
            {intervals.map((interval, index) => {
              return [
                <SummaryTableRows
                  interval={interval}
                  index={index}
                  summary={summary}
                />
              ];
            })}
          </tr>
        </tbody>
      </table>
    </div>
  );
};

export default SummaryPage;

import React, { Fragment } from "react";

const SummaryTableRows = ({ summary, interval, index }) => {
  return (
    <Fragment>
      <tr>
        <td>{interval}</td>
        <td>{summary.temps[index]}</td>
        <td>{summary.humiditys[index]}</td>
        <td>{summary.tempDevi[index]}</td>
        <td>{summary.humidityDevi[index]}</td>
      </tr>
    </Fragment>
  );
};
export default SummaryTableRows;

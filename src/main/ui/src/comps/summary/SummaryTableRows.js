import React from "react";

const SummaryTableRows = ({ summary, interval, index }) => {
  return (
    <div>
      <td>`${interval}`</td>
      <td>`${summary.temps[index]}`</td>
      <td>`${summary.humiditys[index]}`</td>
      <td>`${summary.tempDevi[index]}`</td>
      <td>`${summary.humidityDevi[index]}`</td>
    </div>
  );
};
export default SummaryTableRows

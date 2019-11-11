import React from "react";

const SummaryPageHeader = ({selectSensor, summary}) => {
    return(
    <div className={"summaryHeader headerButtonContainer"}>
        <h3>Sensors</h3>
        {summary.map((sum, index) => (
            <button key={index} className={"summarySensorSelector"} onClick={() => {selectSensor(index)}}>{sum.roomName}</button>
        ))}
    </div>
)};

export default SummaryPageHeader;

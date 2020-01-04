import React, {Fragment} from "react";
import SummaryTableRows from "./SummaryTableRows";

const SummaryPage = ({summary, user, isLoading, canRender}) => {
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
                            <th></th>
                            <th>Average</th>
                            <th></th>
                            <th>Sample Standard Deviation</th>
                        </tr>
                        <tr>
                            <td><b>Interval </b></td>
                            <td><b>Temp</b></td>
                            <td><b>Humidity</b></td>
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
            <div>
                {!isLoading && summary.roomName !== 'outside' && (
                    <div>
                        <hr/>
                        <h2>Stats with Respect to Outdoor Conditions</h2>
                        <table>
                            <tbody>
                            <tr>
                                <td>
                                    Temperature Pearson Correlation Coefficient
                                </td>
                                <td>
                                    {summary.relation[0]}
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    Humidity Pearson Correlation Coefficient
                                </td>
                                <td>
                                    {summary.relation[1]}
                                </td>
                            </tr>
                            
                            <tr>
                                <td>
                                    Temperature Change Sensitivity Factor
                                </td>
                                <td>
                                    {summary.change[0]}
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    Humidity Change Sensitivity Factor
                                </td>
                                <td>
                                    {summary.change[1]}
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    Historic Temperature Change Sensitivity Factor
                                </td>
                                <td>
                                    {summary.longChange[0]}
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    Historic Humidity Change Sensitivity Factor
                                </td>
                                <td>
                                    {summary.longChange[1]}
                                </td>
                            </tr>
                            </tbody>
                        </table>

                    </div>
                )}
            </div>
        </div>
    );
};

export default SummaryPage;

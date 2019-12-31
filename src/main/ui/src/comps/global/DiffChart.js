import React, { PureComponent } from 'react';
import {
    AreaChart, Area, XAxis, YAxis, Tooltip, Legend, Line, LineChart,
} from 'recharts';

const DiffChart = ({data}) => {
    return(
        <AreaChart
            width={800}
            height={400}
            data={data}
            margin={{
                top: 5, right: 30, left: 20, bottom: 100,
            }}
        >
            <XAxis dataKey="name" tick={<CustomizedAxisTick/>}/>
            <YAxis unit="F"/>
            <Area name="Office" dataKey="office" stroke="#FFBE33" fill="#FFBE33" />
            <Area name="Living Room" dataKey="livingRoom" stroke="#82ca9d" fill="#82ca9d" />
            <Area name="Bedroom" dataKey="bedroom" stroke="#8884d8" fill="#8884d8" />
            <Area name="Server Room" dataKey="serverRoom" stroke="#0c0707" fill="#0c0707" />
            <Area name="Outside" dataKey="outside" stroke="#ff33ff" fill="#ff33ff" />
            <Tooltip />
            <Legend verticalAlign="top"/>
        </AreaChart>
    );
};
export default DiffChart

export const CustomizedAxisTick = ({x, y, stroke, payload}) => {
    return (
        <g transform={`translate(${x},${y})`}>
            <text x={0} y={0} dy={16} textAnchor="end" fill="#666" transform="rotate(-65)">{payload.value}</text>
        </g>
    );
}


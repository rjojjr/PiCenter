import React, { PureComponent } from 'react';
import {
    LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend,
} from 'recharts';

const TempChart = ({data}) => {
    return (
        <LineChart
            width={800}
            height={400}
            data={data}
            margin={{
                top: 5, right: 30, left: 20, bottom: 30,
            }}
        >
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="name" tick={<CustomizedAxisTick/>}/>
            <YAxis />
            <Tooltip />
            <Legend verticalAlign="top"/>
            <Line type="monotone" name="Bedroom" dataKey="bedroom" stroke="#8884d8" activeDot={{ r: 8 }} />
            <Line type="monotone" name="Living Room" dataKey="livingRoom" stroke="#82ca9d" />
            <Line type="monotone" name="Server Room" dataKey="serverRoom" stroke="#0c0707" />
            <Line type="monotone" name="Office" dataKey="office" stroke="#fff633" />
            <Line type="monotone" name="Outside" dataKey="outside" stroke="#ff33ff" />
            <Line type="monotone" name="Heat On" dataKey="heat" stroke="#ff3f33" />
        </LineChart>
    );
};

export default TempChart;

export const CustomizedAxisTick = ({x, y, stroke, payload}) =>{
        return (
            <g transform={`translate(${x},${y})`}>
                <text x={0} y={0} dy={16} textAnchor="end" fill="#666" transform="rotate(-65)">{payload.value}</text>
            </g>
        );
    }
"use client";

import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import config from "@/app/config";
import Swal from "sweetalert2";

import DataTable from "datatables.net-react";
import DT from "datatables.net-dt";

import Loading from "@/components/Loading";

DataTable.use(DT);

async function getMaterialRequests(fromDate, toDate, status) {
    // Perform the fetch request
    const response = await fetch(`${config.API_URL}/admin/material-requests?status=${status}&dateFrom=${fromDate}&dateTo=${toDate}`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${localStorage.getItem("jwt_token")}`,
        },
    });

    // Wait for the response to be processed
    const data = await response.json(); // Assuming the response is JSON

    console.log(data);
    return data; // Return the data if needed
}

export default function ManageRequests() {
    const [userData, setUserData] = useState(null);
    const [dataRows, setDataRows] = useState(null);

    // Format the dates
    const todayFormatted = new Date().toISOString().split("T")[0];
    const lastYearFormatted = new Date(new Date().setFullYear(new Date().getFullYear() - 1)).toISOString().split("T")[0];

    const [fromDate, setFromDate] = useState(lastYearFormatted); // State for the "From Date"
    const [toDate, setToDate] = useState(todayFormatted); // State for the "To Date"
    const [status, setStatus] = useState("ALL"); // State for the status filter

    const handleSubmit = () => {
        getMaterialRequests(fromDate, toDate, status).then((data) => {
            // console.log("data", data);
            setDataRows(data);
        });

        // Add your filtering logic or API call here
    };

    const router = useRouter();

    const Toast = Swal.mixin({
        toast: true,
        position: "top-end",
        showConfirmButton: false,
        timer: 3000,
        timerProgressBar: true,
        didOpen: (toast) => {
            toast.onmouseenter = Swal.stopTimer;
            toast.onmouseleave = Swal.resumeTimer;
        },
    });
    // Fetch data after the component has mounted
    useEffect(() => {
        if (typeof window !== "undefined") {
            // Access localStorage or cookies here
            const userDetailStr = localStorage.getItem("user_detail");
            if (userDetailStr) {
                try {
                    const userDetail = JSON.parse(userDetailStr);
                    setUserData(userDetail);
                    handleSubmit();
                } catch (error) {
                    console.error("Error parsing user detail from localStorage:", error);
                }
            }
        }
    }, []); // Runs once after component mounts

    if (!userData) {
        return <Loading />; // Show loading until data is available
    }

    const columns = [
        { data: "requestId", title: "Request Id" },
        { data: "submitDate", title: "Submit Date" },
        { data: "submitBy", title: "Submit By" },
        { data: "status", title: "Status" },
        {
            data: null,
            title: "Actions",
            // render: (rowData) => {
            //     // Render React-like buttons with data attributes for row data
            //     return `
            //         <button class="edit-button" data-id="${rowData.requestId}">Edit</button>
            //         <button class="delete-button" data-id="${rowData.requestId}">Delete</button>
            //     `;
            // },
            orderable: false,
            searchable: false,
        },
    ];

    const columnDefs = [{ orderSequence: ["desc"], targets: [0] }];

    const onDetail = (row) => {
        router.push(`/manage-requests/detail?requestId=${row.requestId}`); // Navigate to the edit page
    };

    return (
        <div>
            <main className="p-4 align-text-center">
                <h1>Manage Requests History</h1>

                <div className="bg-black p-3 rounded">
                    <div className="d-flex justify-content-start align-items-center gap-3">
                        <div className="input-group d-flex flex-column">
                            <label htmlFor="from-date" className="mb-1">
                                From Date
                            </label>
                            <input id="from-date" type="date" value={fromDate} onChange={(e) => setFromDate(e.target.value)} />
                        </div>

                        <div className="input-group d-flex flex-column">
                            <label htmlFor="to-date" className="mb-1">
                                To Date
                            </label>
                            <input id="to-date" type="date" value={toDate} onChange={(e) => setToDate(e.target.value)} />
                        </div>

                        <div className="input-group d-flex flex-column">
                            <label htmlFor="select-status" className="mb-1">
                                Status
                            </label>
                            <select id="select-status" value={status} onChange={(e) => setStatus(e.target.value)}>
                                <option value="ALL">ALL</option>
                                <option value="PENDING_APPROVAL">Pending Approval</option>
                                <option value="APPROVED">Approved</option>
                                <option value="REJECTED">Rejected</option>
                            </select>
                        </div>

                        <button className="btn btn-primary" onClick={handleSubmit}>
                            GO
                        </button>
                    </div>
                    <DataTable
                        className="table table-striped"
                        data={dataRows}
                        columns={columns}
                        order={[[1, "desc"]]}
                        columnDefs={columnDefs}
                        slots={{
                            3: (data, row) => (
                                <div>
                                    {row.status == "PENDING_APPROVAL" ? (
                                        <span className="badge text-bg-warning">PENDING APPROVAL</span>
                                    ) : row.status == "APPROVED" ? (
                                        <span className="badge text-bg-success">APPROVED</span>
                                    ) : (
                                        <span className="badge text-bg-danger">REJECTED</span>
                                    )}
                                </div>
                            ),
                            4: (data, row) => (
                                <div>
                                    <button className="btn btn-warning me-2" onClick={() => onDetail(row)}>
                                        Detail
                                    </button>
                                </div>
                            ),
                        }}
                    >
                        <thead>
                            <tr>
                                <th width="3%">requestId</th>
                                <th width="15%">submitAt</th>
                                <th width="15%">SubmitBy</th>
                                <th width="5%">status</th>
                                <th width="3%">action</th>
                            </tr>
                        </thead>
                    </DataTable>
                </div>
            </main>
        </div>
    );
}

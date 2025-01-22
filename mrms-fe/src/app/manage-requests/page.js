"use client";

import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import config from "../config";
import Swal from "sweetalert2";

import DataTable from "datatables.net-react";
import DT from "datatables.net-dt";

import Loading from "@/components/Loading";

DataTable.use(DT);

async function getMaterialRequests() {
    // Format the dates
    const todayFormatted = new Date().toISOString().split("T")[0];
    const lastYearFormatted = new Date(new Date().setFullYear(new Date().getFullYear() - 1)).toISOString().split("T")[0];

    // Perform the fetch request
    const response = await fetch(
        `${config.API_URL}/admin/material-requests?status=PENDING_APPROVAL&dateFrom=${lastYearFormatted}&dateTo=${todayFormatted}`,
        {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${localStorage.getItem("jwt_token")}`,
            },
        }
    );

    // Wait for the response to be processed
    const data = await response.json(); // Assuming the response is JSON

    console.log(data);
    return data; // Return the data if needed
}

async function addMaterialRequest() {
    let requestData = {
        items: [],
    };

    try {
        const response = await fetch(`${config.API_URL}/admin/material-request`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json", // Ensure you're sending JSON
                Authorization: `Bearer ${localStorage.getItem("jwt_token")}`, // Authorization token
            },
            body: JSON.stringify(requestData), // Send data in the request body as JSON
        });

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const data = await response.json();

        // console.log(data); // Log the response
        return data; // Return the data if needed for further processing
    } catch (error) {
        console.error("Error adding item:", error);
        throw error; // Optionally throw error to handle it outside this function
    }
}

async function deleteMaterialRequest(requestId) {
    try {
        const response = await fetch(`${config.API_URL}/admin/material-request?requestId=${requestId}`, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json", // Ensure you're sending JSON
                Authorization: `Bearer ${localStorage.getItem("jwt_token")}`, // Authorization token
            },
        });

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const data = await response.json();

        return data; // Return the data if needed for further processing
    } catch (error) {
        console.error("Error adding item:", error);
        throw error; // Optionally throw error to handle it outside this function
    }
}

export default function ManageRequests() {
    const [userData, setUserData] = useState(null);
    const [dataRows, setDataRows] = useState(null);

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
                    getMaterialRequests().then((data) => {
                        // console.log("data", data);
                        setDataRows(data);
                    });
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
                <h1>Manage Requests</h1>

                <div className="bg-black p-3 rounded">
                    <DataTable
                        className="table table-striped"
                        data={dataRows}
                        columns={columns}
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

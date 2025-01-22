"use client";
import { useSearchParams } from "next/navigation";
import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import Swal from "sweetalert2";

import Link from "next/link";
import Loading from "@/components/Loading";
import config from "@/app/config";

async function getRequest(requestId) {
    const response = await fetch(`${config.API_URL}/admin/material-request?requestId=${requestId}`, {
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

async function approveRequest(requestId) {
    const response = await fetch(`${config.API_URL}/admin/material-request/approve?requestId=${requestId}`, {
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

async function rejectRequest(requestId, remark) {
    const response = await fetch(`${config.API_URL}/admin/material-request/reject?requestId=${requestId}&remark=${remark}`, {
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

async function editItem(itemToEdit) {
    try {
        const response = await fetch(`${config.API_URL}/admin/material-request-item`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json", // Ensure you're sending JSON
                Authorization: `Bearer ${localStorage.getItem("jwt_token")}`, // Authorization token
            },
            body: JSON.stringify(itemToEdit), // Send data in the request body as JSON
        });

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const data = await response.json();

        console.log(data); // Log the response
        return data; // Return the data if needed for further processing
    } catch (error) {
        console.error("Error adding item:", error);
        throw error; // Optionally throw error to handle it outside this function
    }
}

export default function () {
    let searchParam = useSearchParams();
    const [userData, setUserData] = useState(null);
    const [data, setData] = useState(null);
    const [items, setItems] = useState(null);

    const [itemName, setItemName] = useState("");
    const [itemQuantity, setItemQuantity] = useState("");
    const [itemDesc, setItemDesc] = useState("");

    let requestId = searchParam.get("requestId");

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

    const router = useRouter();

    // Fetch data after the component has mounted
    useEffect(() => {
        if (typeof window !== "undefined") {
            // Access localStorage or cookies here
            getRequest(requestId)
                .then((result) => {
                    // console.log(result);
                    setData(result); // Handle the result
                    setItems(result.items);
                })
                .catch((error) => {
                    console.error("Error fetching data:", error);
                });
            setUserData(config.GET_USER_CREDENTIALS(localStorage));
        }
    }, []); // Runs once after component mounts

    if (!userData || !data) {
        return <Loading />; // Show loading until data is available
    }

    const onEditItem = (requestId, itemId) => {
        let selectedItem = null;
        items.forEach((element) => {
            if (element.itemId == itemId) {
                selectedItem = element;
            }
        });

        selectedItem.requestId = requestId;
        editItem(selectedItem)
            .then((res) => {
                Toast.fire({
                    icon: "success",
                    title: res.message,
                });
                getRequest(requestId).then((res) => {
                    setData(res);
                    setItems(res.items);
                });

                setItemName("");
                setItemQuantity("");
                setItemDesc("");
            })
            .catch((err) => {
                console.log(err);
            });
    };

    const onApprove = () => {
        approveRequest(requestId)
            .then((res) => {
                Toast.fire({
                    icon: "success",
                    title: res.message,
                });

                router.push("/manage-requests");
            })
            .catch((err) => {
                console.log(err);
            });
    };

    const onReject = () => {
        Swal.fire({
            title: "Remark",
            input: "text",
            inputLabel: "please add additional remark",
            // inputValue,
            showCancelButton: true,
            // inputValidator: (value) => {
            //     if (!value) {
            //         return "You need to write something!";
            //     }
            // },
        }).then((result) => {
            if (result.isConfirmed) {
                rejectRequest(requestId, result.value)
                    .then((res) => {
                        Toast.fire({
                            icon: "success",
                            title: res.message,
                        });
                        router.push("/manage-requests");

                        setItemName("");
                        setItemQuantity("");
                        setItemDesc("");
                    })
                    .catch((err) => {
                        console.log(err);
                    });
            }
        });
    };

    const handleInputChange = (itemId, field, value) => {
        const newItems = [...items];

        newItems.forEach((element) => {
            if (element.itemId == itemId) {
                element[field] = value;
            }
        });
        setItems(newItems); // Update the state
        console.log(items);
    };

    return (
        <div className="p-4 align-text-center">
            <div className="d-flex justify-content-between">
                <Link href={"/manage-requests"} className="btn btn-dark mb-2">
                    Back
                </Link>
                <div className={data.status == "PENDING_APPROVAL" ? "d-block" : "d-none"}>
                    <button onClick={onReject} className="btn btn-danger me-2">
                        <i className="bi bi-x-circle me-1"></i>Reject
                    </button>
                    <button onClick={onApprove} className="btn btn-success">
                        <i className="bi bi-check-circle me-1"></i>
                        Approve
                    </button>
                </div>
            </div>

            <h1>Request Id : {requestId}</h1>
            {data.status == "PENDING_APPROVAL" ? (
                <span className="fs-7 badge text-bg-warning">PENDING APPROVAL</span>
            ) : data.status == "APPROVED" ? (
                <>
                    <span className="badge text-bg-success">APPROVED</span>
                    <div>
                        <div className="d-flex">
                            <div style={{ width: "200px" }}>Approved By</div>: {data.approvedBy}
                        </div>
                        <div className="d-flex">
                            <div style={{ width: "200px" }}>Approved Date</div>: {data.approvedDate}
                        </div>
                    </div>
                </>
            ) : (
                <>
                    <span className="badge text-bg-danger">REJECTED</span>
                    <div>
                        <div className="d-flex">
                            <div style={{ width: "200px" }}>Rejected By</div>: {data.rejectedBy}
                        </div>
                        <div className="d-flex">
                            <div style={{ width: "200px" }}>Rejected Date</div>: {data.rejectedDate}
                        </div>
                        <div className="d-flex">
                            <div style={{ width: "200px" }}>Remark</div>: {data.remark}
                        </div>
                    </div>
                </>
            )}
            <div className="pb-3 ">
                <div className="d-flex">
                    <div style={{ width: "200px" }}>Submit By</div>: {data.submitBy}
                </div>
                <div className="d-flex">
                    <div style={{ width: "200px" }}>Submit Date</div>: {data.submitDate}
                </div>
            </div>

            <div className="bg-white p-3 rounded">
                <table className="table table-borderless">
                    <thead>
                        <tr className="text-center">
                            <th width="3%">Item Id</th>
                            <th className="text-start" width="20%">
                                Material Name
                            </th>
                            <th width="6%">Requested Quantity</th>
                            <th className="text-start" width="30%">
                                Usage Description
                            </th>
                            <th width="5%">Actions</th>
                        </tr>
                    </thead>

                    <tbody>
                        {items.map((item, index) => {
                            return (
                                <tr key={item.itemId}>
                                    <td>
                                        <input disabled className="form-control" value={item.itemId} readOnly />
                                    </td>
                                    <td>
                                        <input
                                            className="form-control"
                                            value={item.materialName}
                                            onChange={(e) => handleInputChange(item.itemId, "materialName", e.target.value)}
                                        />
                                    </td>
                                    <td>
                                        <input
                                            className="form-control"
                                            value={item.requestedQuantity}
                                            onChange={(e) => handleInputChange(item.itemId, "requestedQuantity", e.target.value)}
                                        />
                                    </td>
                                    <td>
                                        <input
                                            className="form-control"
                                            value={item.usageDescription}
                                            onChange={(e) => handleInputChange(item.itemId, "usageDescription", e.target.value)}
                                        />
                                    </td>
                                    <td>
                                        <div className={data.status == "PENDING_APPROVAL" ? "d-flex justify-content-center" : "d-none"}>
                                            <button
                                                className="w-100 btn btn-outline-warning me-3"
                                                onClick={() => {
                                                    onEditItem(requestId, item.itemId);
                                                }}
                                            >
                                                Edit
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                            );
                        })}
                    </tbody>
                </table>
            </div>
        </div>
    );
}

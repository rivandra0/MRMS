"use client";

import NavBarTop from "@/components/NavBarTop";
import { useState, useEffect } from "react";

export default function DashboardLayout({ children }) {
    const [userDetail, setUserDetail] = useState(null);

    useEffect(() => {
        if (typeof window !== "undefined") {
            // Only access localStorage on the client side
            const userDetailStr = localStorage.getItem("user_detail");
            if (userDetailStr) {
                setUserDetail(JSON.parse(userDetailStr));
            }
        }
    }, []);

    if (!userDetail) {
        return (
            <div className="h1 d-flex justify-content-center align-items-center h-100">
                <div>Loading...</div>
            </div>
        );
    }

    return (
        <>
            <NavBarTop authorizedPages={userDetail.authorizedPages} />
            <div className="container-fluid">
                <div className="row">{children}</div>
            </div>
        </>
    );
}

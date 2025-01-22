"use client";

import config from "./config";
import { useState } from "react";
import styles from "./page.module.css";
import Swal from "sweetalert2";
import { useRouter } from "next/navigation";

export default function Auth() {
    const router = useRouter();
    const [userId, setUserId] = useState("");
    const [password, setPassword] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const res = await fetch(`${config.API_URL}/auth/login`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    userId: userId,
                    password: password,
                }),
            });

            if (!res.ok) {
                throw res;
            }

            let load = await res.json();
            console.log(load);
            localStorage.setItem("jwt_token", load.message);
            localStorage.setItem("user_detail", JSON.stringify(load.data));
            Swal.fire({
                title: "Successfully logged in!",
                // text: "You have successfully implemented SweetAlert2!",
                icon: "success",
                // confirmButtonText: "OK",
            });
            router.push(load.data.authorizedPages.split(",")[0].split("__")[0]);
        } catch (error) {
            Swal.fire({
                title: error.message || "Please make sure the credentials!",
                icon: "error",
            });
            console.error("Error during login:", error);
        }
    };

    return (
        <div className={styles.page}>
            <main className={styles.main}>
                <h1>LOGIN MRMS</h1>
                <form onSubmit={handleSubmit}>
                    <div className="mb-1">
                        <label htmlFor="input-userid" className="form-label">
                            User Id
                        </label>
                        <input
                            type="text"
                            className="form-control"
                            id="input-userid"
                            placeholder="name@example.com"
                            value={userId}
                            onChange={(e) => setUserId(e.target.value)} // Update state on input change
                        />
                    </div>
                    <div className="mb-3">
                        <label htmlFor="input-password" className="form-label">
                            Password
                        </label>
                        <input
                            type="password"
                            className="form-control"
                            id="input-password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)} // Update state on input change
                        />
                    </div>
                    <button type="submit" className="btn btn-primary">
                        Login
                    </button>
                </form>
            </main>
        </div>
    );
}

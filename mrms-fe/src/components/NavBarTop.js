import Link from "next/link";

export default function NavBarTop({ authorizedPages }) {
    authorizedPages.split(",").forEach((element) => {
        console.log(element.split("__")[0]);
        console.log(element.split("__")[1]);
    });
    return (
        <nav className="navbar navbar-light bg-light p-3">
            <div className="d-flex col-12 col-md-3 col-lg-2 mb-2 mb-lg-0 flex-wrap flex-md-nowrap justify-content-between">
                <div>
                    <a className="navbar-brand" href="#">
                        MRMS
                    </a>
                    <div className="text-black opacity-50">
                        Material Request Management System
                    </div>
                </div>
            </div>
            <div className="col-12 col-md-9 col-lg-10 text-black">
                {authorizedPages.split(",").map((pg, index) => {
                    const [url, name] = pg.split("__");
                    return (
                        <Link
                            className="mx-3 text-decoration-underline"
                            href={url}
                            key={index}
                        >
                            {name}
                        </Link>
                    );
                })}
            </div>
            <div className="col-12 col-md-5 col-lg-8 d-flex align-items-center justify-content-md-end mt-3 mt-md-0">
                {/* <div className="dropdown">
                    <button
                        className="btn btn-secondary dropdown-toggle"
                        type="button"
                        data-toggle="dropdown"
                        aria-expanded="false"
                    >
                        Hello User
                    </button>
                </div> */}
            </div>
        </nav>
    );
}

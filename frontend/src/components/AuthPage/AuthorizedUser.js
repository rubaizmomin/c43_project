import { Navigate, Outlet } from 'react-router-dom';

const AuthorizedUser = () =>{
    // Get user_id
    const u_id = JSON.parse(localStorage.getItem("u_id")) || "";
    console.log(`User ID: ${u_id}`);

    if (u_id === "") {
        console.log("Can't find user information");
        return <Navigate to="/" />;
    }
    console.log("User authorized")
    return <Outlet />;
};

export default AuthorizedUser;
import { BrowserRouter, Route, Routes } from 'react-router-dom';
// Components
import TopBar from './components/TopBar/TopBar';
import AuthorizedUser from './components/AuthPage/AuthorizedUser';
import LandingPage from './components/AuthPage/LandingPage';
import RegisterPage from './components/AuthPage/RegisterPage';
import LoginPage from './components/AuthPage/LoginPage';
import HomePage from './components/HomePage/HomePage';
import AddListingPage from './components/ListingPage/AddListingPage';
// Style
import './App.css';

function App() {
  return (
    <div className="mybnb">
      <BrowserRouter>
        <TopBar />
        <Routes>
          <Route path="/" element={<LandingPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/mybnb" element={<AuthorizedUser />}>
            <Route path="/mybnb" element={<HomePage />} />
            <Route path="/mybnb/addlisting" element={<AddListingPage />} />
          </Route>
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;

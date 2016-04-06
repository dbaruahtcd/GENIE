<?php
if(isset($cookie_name))
{
    // Check if the cookie exists
if(isset($_COOKIE[$cookie_name]))
    {
    parse_str($_COOKIE[$cookie_name]);
 
    // Make a verification
 
    if(($usr == $config_username) && ($hash == md5($config_password)))
        {
        // Register the session
        $_SESSION['email'] = $config_username;
        }
    }
}
?>
module.exports = ({ }) => {
    const execSync = require('child_process').execSync
    execSync(`npm install nodemailer`)
    const nodemailer = require('nodemailer')
    const transporter = nodemailer.createTransport({
        host: "smtp.gmail.com",
        port: 587,
        secureConnection: false,
        auth: {
            user: `${process.env.MAIL_USERNAME}`,
            pass: `${process.env.MAIL_PASSWORD}`
        },
        tls: {
            ciphers: 'SSLv3'
        }
    });
    const report = require('fs').readFileSync('reports/dependencyUpdates/report.html', 'utf8')

    const mailOptions = {
        from: {
            name: 'App Template',
            address: process.env.MAIL_USERNAME
        },
        to: 'mr.vikram004@gmail.com',
        subject: 'Dependency update report of App Template',
        text: `${report}`
    };

    transporter.sendMail(mailOptions, (error, info) => {
        if (error) {
            console.log(error)
        }
    });
}

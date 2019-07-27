package org.nothingugly.uglydeals;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    private Button buttonAccountFragmentLogout;
    private Button buttonAccountFragmentTAC;
    private Button buttonAccountFragmentAbout;
    private Button buttonAccountFragmentContact;
    private Button buttonAccountFragmentPrivacy;
    private FirebaseAuth mAuth;



    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        //Get the firebase authentication instance
        mAuth = FirebaseAuth.getInstance();

        //Get the button from the layout
        buttonAccountFragmentLogout = view.findViewById(R.id.buttonAccountFragmentLogout);
        buttonAccountFragmentTAC = view.findViewById(R.id.buttonAccountFragmentProfileTAC);
        buttonAccountFragmentAbout = view.findViewById(R.id.buttonAccountFragmentProfileAbout);
        buttonAccountFragmentContact = view.findViewById(R.id.buttonAccountFragmentContact);
        buttonAccountFragmentPrivacy = view.findViewById(R.id.buttonAccountFragmentPrivacy);

        //When the logout button is clicked
        buttonAccountFragmentLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                sendToLogin();

            }
        });

        buttonAccountFragmentTAC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setTitle("Terms and Conditions");
                builder1.setMessage(" Welcome to Ugly Deals! The following Terms of Use (“Terms”, “Terms of Use”) constitute an agreement made between you, the website user (“you”, “your,”) and us, Ugly Deals (“Ugly Deals”, “we”, “us”, “our”). By accessing and using this website www.uglydeals.co; including the Ugly Deals mobile application (“Ugly Deals App”); you are agreeing that you have read, understood, and accepted all of the Terms, as may be updated from time to time and agree to be bound by them. If you disagree with any part of the Terms then you may not access the Services. \n" +
                        "\n" +
                        "By using our Services, and subsequently agreeing to the Terms, you are stating that you are a student at either a registered Bangladesh High School, or Tertiary Education Provider or a paid subscriber of the Ugly Deals app. If it is found that you are not a student of such institutes or a paid subscriber, then Ugly Deals reserves the right to remove your account. \n" +
                        "\n" +
                        "You should check this page regularly to take notice of any changes we may have made to the Terms of Use.\n" +
                        "\n" +
                        "General:\n" +
                        "Access to our App and/or Website is permitted on a temporary basis, and we reserve the right to withdraw or amend our Services without notice. We will not be liable if for any reason this Website is unavailable at any time or for any period. From time to time, we may restrict access to some parts or all of our App and/or Website. Our App and/or Website may contain links to other third-party websites (“Third Party Sites”), which are not operated by us. We have no control over the Third Party Sites and/or services and accept no responsibility for them or for any loss or damage that may arise from your use of them. Your use of the Third Party Sites and/or services will be subject to the terms of use and service contained within each such site and/or partners retail. Our Services may also contain logos, content or advertisements of third parties who sponsor our Services to you. We will not be liable for any content of and any representations made by them. A breach of any of the Terms may result in us immediately terminating your access to our App, including your account. You agree not to copy, reproduce, sell, resell or exploit any portion of our services and/or App, use of the services or access to the services without our express written permission.\n" +
                        "\n" +
                        "Access and Use:\n" +
                        "You are responsible for obtaining access to the services which may involve third party fees, such as internet access fees. You must not misuse the services. You will not: use the App or Website for any other part of the services for any illegal or unauthorised purpose; commit or encourage a criminal offense; violate any laws in Bangladesh(including copyright laws); transmit or distribute a virus, trojan, worm, logic bomb or any other material which is malicious, technologically harmful, in breach of confidence or in any way offensive or obscene; hack into any aspect of our Services; corrupt data; cause annoyance to other users; infringe upon the rights of any other person's proprietary rights; or attempt to affect the performance or functionality of any computer facilities of or accessed through this Website. When you register as a user of the App, you must not impersonate any person or entity or provide false information. You will be totally responsible for any activity that takes place under your name, email and password. Please notify us immediately if you become aware of any unauthorised use of your email and password. You consent to Ugly Deals providing you with communications related to the App such as announcements and administrative notices. You may opt out of such communications when we send these emails to you. You consent to Ugly Deals gathering and storing your location data, when your device’s location feature is set to active, for the purpose of Ugly Deals to utilise this data in order to provide you with accurate and relevant information associated with your proximity. Ugly Deals’ use of emails and location data has the sole intention of providing the user with tailored and advantageous information. We will not be liable for any loss or damage caused by a distributed denial-of-service attack, viruses or other technologically harmful material that may infect your computer equipment, computer programs, data or other proprietary material due to your use of our App and/or Website or to your downloading of any material posted on it, or on any website linked to it.\n" +
                        "\n" +
                        "Intellectual Property, Software and Content:\n" +
                        "The intellectual property rights in all software and content made available to you on or through our App and/or Website remain the property of Ugly Deals, its licensors or Ugly Deals Advertisers (depending on the material). All such rights are reserved by Ugly Deals, its licensors or Ugly Deals Advertisers as the case may be. You may store, print and display the content supplied solely for your own personal use. You are not permitted to publish, manipulate, distribute or otherwise reproduce, in any format, any of the content or copies of the content supplied to you or which appears on our App and/or Website nor may you use any such content in connection with any business or commercial enterprise. You must not upload or publish as part of your use of the services, any material that infringes a third-party’s intellectual property rights. Ugly Deals will not be liable for any infringement of third party intellectual property rights that may occur as part of your use of our Services.\n" +
                        "\n" +
                        "Disclaimer of Liability:\n" +
                        "The material displayed on our App and/or Website is provided without any guarantees, conditions or warranties as to its accuracy, completeness or timeliness. You agree that from time to time we may remove the services for indefinite periods of time or cancel the services at any time, without notice to you. We do not guarantee, represent or warrant that your use of our services will be uninterrupted, timely, secure, error-free, nor that the results that may be obtained from the use of the Services will be accurate or reliable. Unless expressly stated to the contrary to the fullest extent permitted by law Ugly Deals and its suppliers, content providers and advertisers hereby expressly exclude all conditions, warranties and other terms which might otherwise be implied by statute, common law or the law of equity and shall not be liable for any damages whatsoever, including but without limitation to any direct, indirect, special, consequential, punitive or incidental damages, or damages for loss of use, profits, data or other intangibles, damage to goodwill or reputation, or the cost of procurement of substitute goods and services, arising out of or related to the use, inability to use, performance or failures of our App or Website or the Third Party Sites and any materials posted thereon, irrespective of whether such damages were foreseeable or arise in contract, tort, equity, restitution, by statute, at common law or otherwise. This does not affect Ugly Deals’ liability for any other liability which cannot be excluded or limited under applicable law.\n" +
                        "\n" +
                        "Linking to our App or Website:\n" +
                        "You may link to our App download on App store or Playstore or our Website home page, provided you do so in a way that is fair and legal and does not damage our reputation or take advantage of it, but you must not establish a link in such a way as to suggest any form of association, approval or endorsement on our part where none exists. You must not establish a link from any website that is not owned by you. Our App and Website must not be framed on any other site, nor may you create a link to any part of our Website other than the home page. We reserve the right to withdraw linking permission without notice.\n" +
                        "\n" +
                        "Disclaimer as to ownership of intellectual property\n" +
                        "Except where expressly stated to the contrary all persons (including their names and images), third party trademarks and content, services and/or locations featured on our App or Website or are in no way associated, linked or affiliated with us and you should not rely on the existence of such a connection or affiliation. Any trade marks/names featured on our App and Website are owned by the respective trademark owners. Where a trademark or brand name is referred to it is used solely to describe or identify the products and services and is in no way an assertion that such products or services are endorsed by or connected to us.\n" +
                        "\n" +
                        "Indemnity:\n" +
                        "You agree to indemnify, defend and hold harmless Ugly Deals, its directors, officers, employees, consultants, agents, and affiliates, from any and all third party claims, liability, damages and/or costs (including, but not limited to, legal fees) arising from your use of our App and Website or your breach of the Terms.\n" +
                        "\n" +
                        "Variation:\n" +
                        "Ugly Deals shall have the right in its absolute discretion at any time and without notice to amend, remove or vary the Services and/or any page of our App and Website.\n" +
                        "\n" +
                        "Invalidity:\n" +
                        "If any part of the Terms is unenforceable (including any provision in which we exclude our liability to you) the enforceability of any other part of the Terms will not be affected all other clauses remaining in full force and effect. So far as possible where any clause/sub-clause or part of a clause/sub-clause can be severed to render the remaining part valid, the clause shall be interpreted accordingly. Alternatively, you agree that the clause shall be rectified and interpreted in such a way that closely resembles the original meaning of the clause /sub-clause as is permitted by law.\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "Deals:\n" +
                        "The Ugly Deals App permits its users to redeem the following Deals: Regular Deal, Flash Deal, provided the required conditions are met. The following deals exist on the Ugly Deals App:\n" +
                        "\n" +
                        "Regular Deal:\n" +
                        "A 'Regular Deal' is a deal which is live on the Ugly Deals platform that can be redeemed via scanning an in-store QR-code from the associated partner.\n" +
                        "\n" +
                        "Flash Deal:\n" +
                        "A ‘Flash Deal' is a Deal which is live on the Ugly Deals platform and is only available until: the specified time is extinguished (e.g. ‘for the next hour’ and the hour has passed), the quantity depleted (e.g. the Ugly Deals Advertiser has no more of the specified Flash Deal), or maximum customer limit reached (e.g. ‘the next 20 students’ and 20 have come and redeemed the Instant Deal). The aforementioned are instances in which the Flash Deal becomes unavailable. In such a circumstance Ugly Deals holds no responsibility for any loss or damage that may arise from this situation. A Flash Deal is redeemed via scanning an in-store QR-code from the associated partner, or by submitting a promo code to the website of the associated partner.\n" +
                        "\n" +
                        "Complaints:\n" +
                        "We operate a complaints handling procedure which we will use to try to resolve disputes when they first arise, please let us know at info@uglydeals.co if you have any complaints or comments. \n" +
                        "\n" +
                        "Please note that Ugly Deals does not take any responsibility for a Ugly Deals Advertiser's ability to provide the good or service stated in the Regular Deal/Featured Deal/Flash Deal. If the Regular Deal/Featured Deal/Flash Deal is unavailable, for whatever reason, the Ugly Deals Advertiser reserves the right to substitute the Regular Deal/Featured Deal/Flash Deal for a deal of equal or greater value, subject to Bangladesh legislation. This means that if the Ugly Deals Advertiser does not have the good or service displayed in the Regular Deal/Featured Deal/Flash Deal then the dispute must be resolved between the two parties of the customer and the Ugly Deals Advertiser - Ugly Deals will not be held liable for any loss or damage that may arise from this situation.\n" +
                        "\n" +
                        "Waiver:\n" +
                        "If you breach these conditions and we take no action, we will still be entitled to use our rights and remedies in any other situation where you breach these conditions.\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "Entire Agreement:\n" +
                        "The above Terms of Use constitute the entire agreement of the parties and supersedes any and all preceding and contemporaneous agreements between you and Ugly Deals.\n");


                builder1.setPositiveButton(
                        "Got it!",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });


                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });

        buttonAccountFragmentPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setTitle("Privacy Policy");
                builder1.setMessage(
                        "This Privacy Policy explains how Ugly Deals App (“Ugly Deals”, “we”, “us”, or “our”) collects, stores, uses and discloses your personal information. We are committed to protecting the privacy of any personal information we hold about any of our users in accordance with the Privacy policies of Bangladesh.\n" +
                        "We are responsible for the collection of your Personal Information for our services (“Services”), including the Ugly Deals mobile application services. We use your personal information for providing and improving the Services. By using the Services, you agree to the collection and use of information in accordance with this policy.\n" +
                        "\n" +
                        "Personal information we collect:\n" +
                        "* When you register as a user of our services, we will collect various types of personal information that you provide to us. Some of the common types of personal information that we may collect include:\n" +
                        "* Contact details (e.g. last name, first name, and e-mail)\n" +
                        "* Education (e.g. university, degree and majors)\n" +
                        "* Demographic information (e.g. gender, address and ethnicity)\n" +
                        "* Information about your hobbies and interests\n" +
                        "* Any other information we ask you to provide when you register as a user;\n" +
                        "* Correspondence and records relating to our dealings with you (e.g. letters, emails and telephone calls)\n" +
                        "* If you cannot or will not provide us with the personal information we reasonably require, we may not be able to provide you with the services or assistance you require.\n" +
                        "\n" +
                        "Log Data:\n" +
                        "We collect information that your browser sends whenever you visit our Services (\"Log Data\"). This Log Data may include information such as your computer's Internet Protocol (\"IP\") address, browser type, browser version, the pages of our Services that you visit, the time and date of your visit, the time spent on those pages and other statistics. In addition, we may use third party services such as Google Analytics that collect, monitor and analyze this type of information in order to increase our Service's functionality. These third party service providers have their own privacy policies addressing how they use such information. When you access the Services by or through a mobile device, we may collect certain information automatically, including, but not limited to, the type of mobile device you use, your mobile devices unique device ID, the IP address of your mobile device, your mobile operating system, the type of mobile Internet browser you use and other statistics.\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "Cookies:\n" +
                        "Cookies are anonymous data files stored on your computer after you access certain websites. There are merely used to identify visitors when they return to us, so that certain information already provided by the visitor to a site is not required to be provided again. Cookies are also used to gather data on which areas of a site are visited frequently and which are not, which help us plan and enhance our sites. We acknowledge that some users may wish to disable cookies. This can be done by changing your web browser settings and/or deleting them from your hard drive. Visit an appropriate website to find out more about cookies.\n" +
                        "\n" +
                        "Why we collect personal information:\n" +
                        "* We will use your personal information for purposes including (but not limited to):\n" +
                        "* To provide targeted advertising in accordance with the information that you provide;\n" +
                        "* Handling questions or complaints that you have about us or our Services;\n" +
                        "* Improving our Services (for example, by using information about your printing habits to get a better understanding of your needs and desires);\n" +
                        "* Conducting competitions, promotions and trade incentive programs that you have entered;\n" +
                        "* Provide your email address and name to our partners and advertisers from whom you consent, when registering with us or from time to time when asked, to receive newsletters, promotions or offers from our partners or advertisers;\n" +
                        "* Securing and improving your use of our websites; and\n" +
                        "* Complying with applicable legal requirements.\n" +
                        "* How we collect your personal information\n" +
                        "* We may collect your personal information directly from you when you register with us as a user of our Services. We may also collect your personal information when you provide it to us through other means, for example:\n" +
                        "* When you contact us for any reasons\n" +
                        "* When you browse our websites and social media sites\n" +
                        "* We may also collect personal information about your from publicly available source or from third parties. Where it does so, it will ensure that we act in accordance with relevant privacy laws.\n" +
                        "\n" +
                        "Disclosing your personal information:\n" +
                        "In some cases, we need to transfer or disclose your personal information to external recipients (some of which may be based outside of Bangladesh). The types of recipients that we share your information with will depend on the nature of the information and why we collected it. In the course of our ordinary business operations we disclose personal information to:\n" +
                        "External service providers (for example third-party IT services (such as data storage services, email filtering, virus scanning, call centers, printers, and external printing and photocopying providers)\n" +
                        "Our advisors and consultants\n" +
                        "Other than those external recipients referred to above, we will not disclose your personal information to any other third party unless it has reasonable grounds to believe:\n" +
                        "You have authorized us to do so;\n" +
                        "Your safety, or the safety of others in the community is at risk; or\n" +
                        "We are required or permitted by law to do so.\n" + "\n" +
                        "Security:\n" +
                        "We have taken and will take steps to ensure personal information we hold about you is protected from risks such as loss, unauthorized access, use destruction, modification or disclosure.\n" +
                        "No data transmission over the internet is totally secure. As a result, any personal information you send to us over the Internet (including via email) is sent at your own risk.\n" +
                        "Information sent by you to our App and/or Website is not encrypted. You acknowledge that we do not guarantee the security of the content of any such information, and it is entirely your responsibility to satisfy yourself as to whether our security measures are sufficient for your requirements.\n" +
                        "\n" +
                        "Storage of Data:\n" +
                        "We use third party service providers to assist in storing and processing certain types of personal information for us, and some of these service providers may be located overseas, or use facilities located overseas to provide us with services.\n" +
                        "Your personal information will not be stored for a time period exceeding the period required or permitted by law.\n" +
                        "\n" +
                        "Links To Other Sites:\n" +
                        "Our Services may contain links to other sites that are not operated by us. If you click on a third party link, you will be directed to that third party's site. We strongly advise you to review the Privacy Policy of every site you visit. We have no control over, and assume no responsibility for the content, privacy policies or practices of any third party sites or services.\n" +
                        "\n" +
                        "Accessing and correcting your personal information:\n" +
                        "You have a right to request access to or correction of your personal information held by us. If you wish to access, correct or update any personal information we may hold about you, please contact us at info@uglydeals.co\n" +
                                "\n"+
                        "Updates:\n" +
                        "We may amend this Privacy Policy at any time and for any reason. The latest version will be available on our website here. You should check this Privacy Policy regularly for changes.\n" +
                                "\n" +
                        "Contact details:\n" +
                        "If you have any questions regarding this Privacy Policy, please contact us at info@uglydeals.co\n");


                builder1.setPositiveButton(
                        "Got it!",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });


                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });

        buttonAccountFragmentAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setTitle("About Ugly Deals!");
                builder1.setMessage("Aim: Ugly Deals aims to be a one stop hub for students to get the best deals ranging from food and lifestyle to jobs. We want to provide the best value all the while making spending a bit more fun. \n" +
                        "\n" +
                        "Vision: We want to encourage a community that is involved in helping and caring for what is around us and ensure we play a part in changing the world for the better. \n" +
                        "\n" +
                        "Company Values: TRAIT (Teamwork, Reliable, Ambitious, Innovation & Trust)  \n" +
                        "\n" +
                        "About: Ugly Deals is founded by three friends, who have always upheld similar views on the social context. They all lived in different parts of the world with different experiences which helped each of them to refine their ideologies and care more about their own community where they grew up. So, they shared their ideas and founded Ugly Deals to take their visions forward through an App based platform. \n" +
                        "\n" +
                        "What is the App? Ugly Deals App or UD App provides exclusive offers and deals for students.  There will be five featured deals along with the regular deals. There will also be flash deals, which will provide an extra incentive for the users as they can participate in monthly draws. \n" +
                        "In addition, a points based system will be incorporated which will result in a point to redeem scheme in the future. Although it is an App primarily for students, it is not exclusive only to students. Any smartphone user will be able to use the UD App in a monthly/yearly subscription method.\n" +
                        "\n" +
                        "How it works:\n" +
                        "1. Download App from Play store or App store \n" +
                        "2. Make an account with us in less than 2 minutes\n" +
                        "3. Search for the deal from your favourite restaurants\n" +
                        "4. Click on the deal\n" +
                        "5. Scan & Redeem! \n" +
                        "\n" +
                        "\n" +
                        "Ugly Deals role: We want to create a more socialising and outgoing culture in Chittagong, for this we want to help restaurants/cafes be more accessible for everyone. Starting with students, who are the majority of the out goers. Ugly Deals wants to drive change for the betterment of our society.");


                builder1.setPositiveButton(
                        "Got it!",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });


                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        });

        buttonAccountFragmentContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setTitle("Contact Us");
                builder1.setMessage("Ugly Deals \n"+
                        "info@uglydeals.co\n"+
                        "\u202D+880 14 0555 1986\n" +
                        "\n" +
                        "We would love to hear from you!");

                builder1.setPositiveButton(
                        "Got it!",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });


                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        });



        return view;
    }

    //Send to LoginActivity
    private void sendToLogin() {
        Intent loginintent = new Intent(getContext(), SplashActivity.class);
        startActivity(loginintent);
        //gets the activity of the fragment and destroys the activity
        getActivity().finish();
    }

}
